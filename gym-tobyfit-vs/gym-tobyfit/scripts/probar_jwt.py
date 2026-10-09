#!/usr/bin/env python3
"""Pruebas HTTP reproducibles contra el servidor en ejecución, sin librerías externas.

Ejemplo: python scripts/probar_jwt.py --output evidencias_http.json
Las cuentas por defecto son únicamente las cuentas ficticias del perfil demo/SQL de demo.
"""
import argparse
import base64
import datetime
import json
import sys
import urllib.error
import urllib.request
from pathlib import Path


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--base-url", default="http://localhost:8080")
    parser.add_argument("--output", default="evidencias_http.json")
    args = parser.parse_args()
    results = []
    # Evita que un proxy corporativo intercepte las peticiones al servidor local.
    client = urllib.request.build_opener(urllib.request.ProxyHandler({}))

    def request(name, method, path, expected, body=None, token=None, check=None):
        headers = {"Content-Type": "application/json"}
        if token is not None:
            headers["Authorization"] = "Bearer " + token
        data = json.dumps(body).encode() if body is not None else None
        req = urllib.request.Request(args.base_url.rstrip("/") + path, data=data, headers=headers, method=method)
        try:
            response = client.open(req, timeout=15)
        except urllib.error.HTTPError as error:
            response = error
        with response:
            status = response.code
            text = response.read().decode("utf-8")
            response_headers = dict(response.headers)
        try:
            content = json.loads(text)
        except json.JSONDecodeError:
            content = text
        passed = status == expected
        if check is not None:
            passed = passed and check(content)
        evidence = content
        if path == "/auth" and status == 200:
            evidence = {"token": "OMITIDO: generar uno nuevo con POST /auth", "longitud": len(text)}
        results.append({"prueba": name, "metodo": method, "ruta": path, "esperado": expected,
                        "recibido": status, "correcto": passed, "respuesta": evidence})
        print(f"{'OK' if passed else 'ERROR'} | {status} | {name}")
        return content, response_headers

    try:
        request("Endpoint de productos sin token", "GET", "/api/productos", 401)
        request("Login con contraseña incorrecta", "POST", "/auth", 401,
                {"username": "admin.jwt@tobyfit.local", "password": "incorrecta"})
        request("Login incompleto", "POST", "/auth", 400, {"username": "admin.jwt@tobyfit.local"})
        admin, _ = request("Login ADMIN y generación del token", "POST", "/auth", 200,
                           {"username": "admin.jwt@tobyfit.local", "password": "DemoAdmin2026!"},
                           check=lambda token: isinstance(token, str) and len(token.split(".")) == 3)
        if not isinstance(admin, str) or len(admin.split(".")) != 3:
            raise RuntimeError("El login ADMIN no produjo un token; revisa las cuentas demo")
        parts = admin.split(".")
        header = json.loads(base64.urlsafe_b64decode(parts[0] + "=" * (-len(parts[0]) % 4)))
        claims = json.loads(base64.urlsafe_b64decode(parts[1] + "=" * (-len(parts[1]) % 4)))
        token_ok = (header.get("alg") == "HS256" and header.get("typ") == "JWT"
                    and claims.get("sub") == "admin.jwt@tobyfit.local"
                    and claims.get("exp", 0) > claims.get("iat", 0)
                    and "password" not in claims and "contrasena" not in claims)
        results.append({"prueba": "Estructura y claims del token", "correcto": token_ok,
                        "header": header, "claims": claims,
                        "nota": "Decodificar no verifica la firma; las peticiones siguientes prueban la aceptación y el rechazo en el servidor."})
        print(f"{'OK' if token_ok else 'ERROR'} | JWT | Estructura y claims")
        request("Productos con token válido", "GET", "/api/productos", 200, token=admin,
                check=lambda body: isinstance(body, list))
        request("Usuarios con ADMIN y sin contraseñas expuestas", "GET", "/api/usuarios", 200, token=admin,
                check=lambda body: isinstance(body, list) and bool(body) and all("contrasena" not in u for u in body))
        parts[2] = ("B" if parts[2].startswith("A") else "A") + parts[2][1:]
        request("Token con firma alterada", "GET", "/api/productos", 401, token=".".join(parts))
        request("Token malformado", "GET", "/api/productos", 401, token="no-es-un-jwt")
        user, _ = request("Login USUARIO", "POST", "/auth", 200,
                          {"username": "usuario.jwt@tobyfit.local", "password": "DemoUsuario2026!"})
        if not isinstance(user, str):
            raise RuntimeError("El login USUARIO no produjo un token")
        request("USUARIO puede leer productos", "GET", "/api/productos", 200, token=user)
        request("USUARIO no puede listar usuarios", "GET", "/api/usuarios", 403, token=user)
        request("Petición posterior sin token sigue rechazada", "GET", "/api/productos", 401)
    except (urllib.error.URLError, RuntimeError, ValueError) as error:
        results.append({"prueba": "Ejecución", "correcto": False, "error": str(error)})
        print("ERROR:", error)
    report = {"fecha_utc": datetime.datetime.now(datetime.timezone.utc).isoformat(),
              "base_url": args.base_url, "total": len(results),
              "correctas": sum(r["correcto"] for r in results), "resultados": results}
    output = Path(args.output)
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Resultado: {report['correctas']}/{report['total']}. Evidencia: {output}")
    return 0 if all(r["correcto"] for r in results) else 1


if __name__ == "__main__":
    sys.exit(main())

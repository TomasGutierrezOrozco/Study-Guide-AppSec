# SQL Injection (SQLI)
query=f"SELECT * FROM users WHERE id={request.args['id']}"
conn.execute(query)

# IDOR
return db.execute('SELECT * FROM invoices WHERE id=?',(request.view_args['id'],)).fetchone()

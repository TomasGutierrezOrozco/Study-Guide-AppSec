# IDOR
def demo():
    return db.execute('SELECT * FROM invoices WHERE id=?', (request.view_args['id'],)).fetchone()

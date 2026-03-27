@app.get("/documents/<int:doc_id>")
def get_document(doc_id):
    if "user_id" not in session:
        return "auth required", 401
    doc = db.execute("SELECT id, owner_id, content FROM documents WHERE id = ?", (doc_id,)).fetchone()
    return dict(doc)

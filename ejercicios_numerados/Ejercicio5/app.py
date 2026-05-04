import yaml
from flask import Flask, request, render_template_string
from lxml import etree

app = Flask(__name__)


@app.get("/")
def home():
    return "Ejercicio5"


@app.get("/hello")
def hello():
    return render_template_string("Hello " + request.args.get("name", "world"))


@app.post("/xml")
def xml():
    parser = etree.XMLParser(resolve_entities=True)
    root = etree.fromstring(request.data, parser=parser)
    return {"tag": root.tag, "text": root.text}


@app.post("/yaml")
def yaml_load():
    return {"result": str(yaml.load(request.data, Loader=yaml.Loader))}


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)

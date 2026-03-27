# XML External Entity Injection (XXE)
root=etree.fromstring(request.data, parser=etree.XMLParser(resolve_entities=True))

FROM python:3.14-slim
WORKDIR /app
COPY app.py /app/app.py
RUN pip install flask pycryptodome
EXPOSE 5000
CMD ["python", "/app/app.py"]

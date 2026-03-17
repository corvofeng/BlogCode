# HTTP Header Viewer

A lightweight Flask app that displays all request headers received by the backend in a key/value table.

## Run locally

```bash
pip install -r requirements.txt
python app.py
```

Open http://localhost:8000

## Build and run with Docker

```bash
docker build -t header-viewer .
docker run --rm -p 8000:8000 header-viewer
```

Then open http://localhost:8000

## Test with custom headers

```bash
curl -H "X-Demo: hello" -H "X-Env: local" http://localhost:8000
```

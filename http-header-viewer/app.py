from flask import Flask, render_template_string, request

app = Flask(__name__)

HTML_TEMPLATE = """
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HTTP Header Viewer</title>
  <style>
    :root {
      --bg: #f4f6fb;
      --card: #ffffff;
      --line: #d9deeb;
      --text: #1f2937;
      --muted: #6b7280;
      --accent: #0f766e;
    }

    * {
      box-sizing: border-box;
    }

    body {
      margin: 0;
      font-family: "Noto Sans", "Segoe UI", sans-serif;
      background: linear-gradient(135deg, #f8fafc, #eef2ff);
      color: var(--text);
      min-height: 100vh;
      display: grid;
      place-items: center;
      padding: 24px;
    }

    .container {
      width: min(960px, 100%);
      background: var(--card);
      border: 1px solid var(--line);
      border-radius: 16px;
      box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
      overflow: hidden;
    }

    .header {
      padding: 18px 20px;
      border-bottom: 1px solid var(--line);
      background: #f8fafc;
    }

    h1 {
      margin: 0 0 6px;
      font-size: 1.2rem;
    }

    .meta {
      color: var(--muted);
      font-size: 0.95rem;
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }

    th,
    td {
      text-align: left;
      padding: 12px 14px;
      border-bottom: 1px solid var(--line);
      vertical-align: top;
      word-break: break-word;
    }

    th {
      background: #f8fafc;
      font-weight: 700;
      color: #111827;
    }

    tr:hover td {
      background: #f0fdfa;
    }

    .key {
      color: var(--accent);
      font-weight: 600;
      width: 32%;
    }

    .empty {
      padding: 20px;
      color: var(--muted);
    }
  </style>
</head>
<body>
  <main class="container">
    <section class="header">
      <h1>HTTP Headers Received by Backend</h1>
      <div class="meta">Method: {{ method }} | Path: {{ path }} | Total Headers: {{ headers|length }}</div>
    </section>

    {% if headers %}
    <table>
      <thead>
        <tr>
          <th>Header Key</th>
          <th>Header Value</th>
        </tr>
      </thead>
      <tbody>
        {% for key, value in headers %}
        <tr>
          <td class="key">{{ key }}</td>
          <td>{{ value }}</td>
        </tr>
        {% endfor %}
      </tbody>
    </table>
    {% else %}
    <div class="empty">No headers received.</div>
    {% endif %}
  </main>
</body>
</html>
"""


@app.route("/", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"])
def show_headers():
    header_items = sorted(request.headers.items(), key=lambda item: item[0].lower())
    return render_template_string(
        HTML_TEMPLATE,
        headers=header_items,
        method=request.method,
        path=request.path,
    )


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)

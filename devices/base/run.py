from application.app import app
from application.config import setup_config
from application.details import load_details

with app.app_context():
    setup_config()
    load_details()

app.run(host='0.0.0.0', port=10000, debug=True)
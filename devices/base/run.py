from application.app import app
from application.config import setup_config
from application.details import load_details
from application.crypto import setup_keys, setup_ssl

with app.app_context():
    setup_config()
    load_details()
    setup_keys()
    setup_ssl()

app.run(host='0.0.0.0', port=10000, debug=False)
from flask import current_app
import os
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import rsa, padding

class CryptographyManager:
    PRIVATE_KEY_PATH = "memory/private_key.pem"
    PUBLIC_KEY_PATH = "memory/public_key.pem"
    SSL_CERT_PATH = "memory/ssl_certificate.pem"
    KEYS_KEY = 'CRYPTO'

    def __init__(self, app):
        self.app = app
        self.private_key = None
        self.public_key = None
        self.private_key_pem = None
        self.public_key_pem = None
        self.ssl_verify = False
        self.protocol = 'http'

    def load_keys(self):
        with open(self.PRIVATE_KEY_PATH) as private_key_file:
            self.private_key = serialization.load_pem_private_key(
                private_key_file.read().encode(),
                password=None,
                backend=default_backend()
            )

        with open(self.PUBLIC_KEY_PATH) as public_key_file:
            self.public_key = serialization.load_pem_public_key(
                public_key_file.read().encode(),
                backend=default_backend()
            )

        self.update_pems()

    def generate_new_keys(self):
        self.private_key = rsa.generate_private_key(
            public_exponent=65537,
            key_size=2048,
            backend=default_backend()
        )
        self.public_key = self.private_key.public_key()

        self.update_pems()

    def export_keys(self):
        with open(self.PRIVATE_KEY_PATH, 'w') as private_key_file:
            private_key_file.write(self.private_key_pem)

        with open(self.PUBLIC_KEY_PATH, 'w') as public_key_file:
            public_key_file.write(self.public_key_pem)

    def sign(self, message):
        return self.private_key.sign(
            message,
            padding.PSS(
                mgf=padding.MGF1(hashes.SHA256()),
                salt_length=20
            ),
            hashes.SHA256()
        ).hex()

    def update_pems(self):
        self.private_key_pem = self.private_key.private_bytes(
            encoding=serialization.Encoding.PEM,
            format=serialization.PrivateFormat.PKCS8,
            encryption_algorithm=serialization.NoEncryption()
        ).decode()
        
        self.public_key_pem = self.public_key.public_bytes(
            encoding=serialization.Encoding.PEM,
            format=serialization.PublicFormat.SubjectPublicKeyInfo
        ).decode()

    def enable_ssl(self):
        self.ssl_verify = self.SSL_CERT_PATH
        self.protocol = 'https'

cryptography_manager = CryptographyManager(current_app)

def setup_keys():
    if os.path.isfile(cryptography_manager.PRIVATE_KEY_PATH):
        cryptography_manager.load_keys()
    else:
        cryptography_manager.generate_new_keys()
        cryptography_manager.export_keys()

def setup_ssl():
    if os.path.isfile(cryptography_manager.SSL_CERT_PATH):
        cryptography_manager.enable_ssl()
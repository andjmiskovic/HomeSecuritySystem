import requests
import json
import random
from flask import current_app
from datetime import datetime, timezone
from application.details import device_details
from application.config import device_config
from application.crypto import cryptography_manager as crypto

alarm_simulation = False
display_data = {}

headers = {
    'Content-Type': 'application/json'
}


def request_pairing(code):
    device_handshake_data = {
        'name': device_details['NAME'],
        'manufacturer': device_details['MANUFACTURER'],
        'type': device_details['TYPE'],
        'macAddress': device_details['MAC_ADDRESS'],
        'label': device_config['LABEL'],
        'publicKey': crypto.public_key_pem,
        'sensors': [{'name': s['name'], 'unit': s['unit'], 'type': s['type']} for s in device_details['SENSORS']],
        'alarms': [row + [''] for row in device_details['ALARMS']]
    }

    current_app.logger.info(f'Requested pairing for code {code}')
    response = requests.post(
        f'{crypto.protocol}://localhost:8001/devices/handshake/device/{code}',
        json=device_handshake_data,
        headers=headers,
        timeout=65,  # seconds
        verify=crypto.ssl_verify
    )

    print(response.text)
    response_data = json.loads(response.text)
    device_config['DEVICE_ID'] = response_data['deviceId']
    device_config.save()

    if (response.status_code == requests.codes.ok):
        current_app.logger.info('Paired successfully.')
        return json.loads(response.text)
    else:
        current_app.logger.error('Pairing timed out.')
        return None


def send_message():
    global display_data
    sensor_data = { sensor['name']: generate_measurement(sensor) for sensor in device_details['SENSORS']}
    display_data = { sensor['name']: f"{round(sensor_data[sensor['name']], 2)} {sensor['unit']}" for sensor in device_details['SENSORS']}

    if not device_config['DEVICE_ID']:
        current_app.logger.info(
            f'Device is not connected to SecureIT. Sending message skipped.')
        return

    message = {
        'measures': sensor_data,
        'timestamp': str(datetime.now(timezone.utc)),
        'deviceId': device_config.device_id
    }

    message_bytes = json.dumps(message).encode('utf-8')
    params = {
        'signature': crypto.sign(message_bytes)
    }

    current_app.logger.info(f'Sending {message["measures"]}')

    response = requests.post(
        f'{crypto.protocol}://localhost:8001/monitor/send',
        json=message,
        params=params,
        headers=headers,
        verify=crypto.ssl_verify
    )

    if (response.status_code == requests.codes.ok):
        current_app.logger.info(f'Server received {json.loads(response.text)["message"]} bytes.')
    else:
        current_app.logger.info(f'Message denied: {json.loads(response.text)["message"]}')


def generate_measurement(sensor):
    key = 'alarm' if alarm_simulation else 'regular'

    if sensor['type'] in ['double', 'float']:
        return random.gauss(
            sensor[key]['mu'],
            sensor[key]['sigma']
        )
    elif sensor['type'] in ['int', 'long']:
        return round(random.gauss(
            sensor[key]['mu'],
            sensor[key]['sigma']
        ))
    elif sensor['type'] in ['string', 'boolean']:
        return sensor[key]

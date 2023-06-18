#!/bin/bash

echo "Copying key pair from backend server"
cp ../../backend/secureit/src/main/resources/localhost_*.pem .

echo "Configuring Angular"
cat <<EOF > ssl-config.js
const fs = require('fs');

module.exports = {
  key: fs.readFileSync('./localhost_key.pem'),
  cert: fs.readFileSync('./localhost_cert.pem'),
};
EOF

echo "Switching protocol to https"
sed -i 's/http:\/\/localhost:8001/https:\/\/localhost:8001/g' src/app/environment.development.ts

echo "Done! You can run the app using: "
echo "  ng serve --ssl true --ssl-key ./localhost_key.pem --ssl-cert ./localhost_cert.pem"

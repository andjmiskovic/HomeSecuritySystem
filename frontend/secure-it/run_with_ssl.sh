#!/bin/bash

echo "Copying key pair from backend server"
cp ../../backend/secureit/src/main/resources/*.pem .

echo "Configuring Angular"
cat <<EOF > ssl-config.js
const fs = require('fs');

module.exports = {
  key: fs.readFileSync('./key.pem'),
  cert: fs.readFileSync('./cert.pem'),
};
EOF

echo "Switching protocol to https"
sed -i 's/http:\/\/localhost:8001/https:\/\/localhost:8001/g' src/app/environment.development.ts

echo "Starting app"
ng serve --ssl true --ssl-key ./key.pem --ssl-cert ./cert.pem

echo "Cleaning up"
rm ssl-config.js cert.pem key.pem

echo "Switching protocol back to http"
sed -i 's/https:\/\/localhost:8001/http:\/\/localhost:8001/g' src/app/environment.development.ts

echo "Bye!"
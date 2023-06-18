echo "Cleaning up"
rm ssl-config.js cert.pem key.pem

echo "Switching protocol back to http"
sed -i 's/https:\/\/localhost:8001/http:\/\/localhost:8001/g' src/app/environment.development.ts

echo "Bye!"
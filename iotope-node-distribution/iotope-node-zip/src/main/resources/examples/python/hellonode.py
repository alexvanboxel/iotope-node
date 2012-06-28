import http.server
import socketserver
import json

PORT = 8811

class MyHandler(http.server.BaseHTTPRequestHandler):
    def do_POST(self):
        len = int(self.headers['Content-Length'])
        print(len)
        print(self.headers['Content-Type'])
        js = self.rfile.read(len)
        print(js)
        iot = json.loads(js.decode("utf-8"))
        tagId = iot["nfcTarget"]["nfcId"]
        print("Received")

        self.send_response(200)
        self.send_header("Content-type", "application/vnd.iotope-0.1+json")
        self.end_headers()

        resp = { "urn:iotope.app:iotope.org:notify" : 
            {   "caption" : "Message for Python" , 
                "message" : "I see a tag with ID "+tagId,
                "type" : "INFO"
                 } }
        self.wfile.write(json.dumps(resp).encode("utf-8"))

try:
    server = http.server.HTTPServer(('localhost', PORT), MyHandler)
    print('Started http server')
    server.serve_forever()
except KeyboardInterrupt:
    print('^C received, shutting down server')
    server.socket.close()




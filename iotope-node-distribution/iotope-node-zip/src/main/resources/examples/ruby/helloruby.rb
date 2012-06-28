# ruby hello.rb -p 8811
require "rubygems"
require "sinatra"
require "json"

post '/*' do
  data = JSON.parse(request.body.read)
  tagId = data["nfcTarget"]["nfcId"]
  
  headers "content_type" => "application/vnd.iotope-0.1+json"
  JSON.generate({
    "urn:iotope.app:iotope.org:notify" => {
      "caption" => "Message for Ruby",
      "message" => "I see a tag with ID #{tagId}",
      "type" => "INFO"
    }
  })
end


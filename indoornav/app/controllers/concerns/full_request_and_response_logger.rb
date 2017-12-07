module FullRequestAndResponseLogger
  extend ActiveSupport::Concern

  included do
    append_around_action :log_request_and_response

    def log_request_and_response
      Rails.logger.info { '  Headers:' }
      %w[accept authorization accept_language user_agent].each do |header|
        header_value = request.send(header)
        Rails.logger.info { "    #{header.titleize.gsub(/\s/, '-')}=#{header_value.inspect}" } if header_value.present?
      end

      begin
        json = JSON.pretty_generate(JSON.parse(request.body.read))
        Rails.logger.info { '  Body:' }
        json.lines.each { |l| Rails.logger.info { "    #{l.chomp}" } }
      rescue Exception => e
      end

      yield

      begin
        json = JSON.pretty_generate(JSON.parse(response.body))
        Rails.logger.info { '  Body:' }
        json.lines.each { |l| Rails.logger.info { "    #{l.chomp}" } }
      rescue Exception => e
      end
    end
    private :log_request_and_response
  end
end

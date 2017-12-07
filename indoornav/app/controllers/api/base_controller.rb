class Api::BaseController < ApplicationController
  # Skip CSRF check
  skip_before_action :verify_authenticity_token

  include FullRequestAndResponseLogger

  respond_to :json

protected
  # Returns logged in user
  def current_user
    @current_user ||= User.find_by(token: request.headers[:authorization])
  end

 # Checks for logged in user
  def authenticate_user!
    unless current_user?
      render status: :unauthorized
    end
  end
end

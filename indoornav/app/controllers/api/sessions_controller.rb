class Api::SessionsController < Api::BaseController
  def create
    respond_with @user = User.authenticate(*session_params.values_at(:username, :password))
  end

protected
  def session_params
    params.require(:session).permit(:username, :password)
  end
end

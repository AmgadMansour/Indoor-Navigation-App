class Api::UsersController < Api::BaseController
  def show
    respond_with @user = User.find(params[:id])
  end

  def create
    @user = User.new(user_params)

    if @user.save
      respond_with @user
    else
      render status: :internal_server_error
    end
  end

  def user_destinations
    user = User.find(params[:id])
    favorites = Favorite.where(user_id: user.id)

    @destinations = []
    favorites.each do |f|
      @destinations += [Destination.find_by(id: f.destination_id)]
    end

    respond_with @destinations
  end

  def update_location
    @user = User.find(params[:id])

    @user.x_coordinate = user_params[:x_coordinate]
    @user.y_coordinate = user_params[:y_coordinate]
    @user.room_id = user_params[:room_id]

    @user.save

    respond_with @user
  end

protected
  def user_params
    params.require(:user).permit(:first_name, :last_name, :day, :month, :year, :gender, :username, :password, :x_coordinate, :y_coordinate, :room_id)
  end
end

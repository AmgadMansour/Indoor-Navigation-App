class Api::RoomsController < Api::BaseController
  def all_rooms
    respond_with @rooms = Room.where(map_id: params[:map_id])
  end

protected
  def rooms_params
    params.require(:rooms).permit(:name, :xmin, :ymin, :xmax, :ymax, :map_id)
  end
end

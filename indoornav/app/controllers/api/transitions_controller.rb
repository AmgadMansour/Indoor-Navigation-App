class Api::TransitionsController < Api::BaseController
  def all_transitions
    respond_with @transitions = Transition.where(map_id: params[:map_id])
  end

protected
  def transition_params
    params.require(:transition).permit(:x_coordinate, :y_coordinate, :map_id, :room_id)
  end
end

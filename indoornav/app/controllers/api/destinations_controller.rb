class Api::DestinationsController < Api::BaseController
  def show
    respond_with @destination = Destination.find(params[:id])
  end

  def all_destinations
    respond_with @destinations = Destination.where(map_id: params[:map_id])
  end

  def destinations_by_category
    respond_with @destinations = Destination.where(category_id: params[:category], map_id: params[:map_id])
  end

protected
  def destination_params
    params.require(:destination).permit(:name, :description, :x_coordinate, :y_coordinate, :map_id, :category_id)
  end
end

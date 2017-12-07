class Api::BeaconsController < Api::BaseController
  def all_beacons
    respond_with @beacons = Beacon.where(map_id: params[:map_id])
  end

protected
  def beacon_params
    params.require(:beacon).permit(:x_coordinate, :y_coordinate, :map_id)
  end
end

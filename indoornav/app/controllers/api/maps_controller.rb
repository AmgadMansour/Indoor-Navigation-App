class Api::MapsController < Api::BaseController
  def show
    respond_with @map = Map.find(params[:id])
  end

protected
  def maps_params
    params.require(:map).permit(:path)
  end
end

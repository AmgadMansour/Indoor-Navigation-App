class Api::GraphsController < Api::BaseController
  def show
    respond_with @graph = Graph.find_by(map_id: params[:map_id])
  end

protected
  def graph_params
    params.require(:graph).permit(:json, :map_id)
  end
end

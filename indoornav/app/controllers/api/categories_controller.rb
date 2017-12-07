class Api::CategoriesController < Api::BaseController
  def all_categories
    respond_with @categories = Category.where(map_id: params[:map_id])
  end

protected
  def category_params
    params.require(:category).permit(:name, :map_id)
  end
end

class RemoveCategoryFromDestination < ActiveRecord::Migration
  def change
  remove_column :destinations, :category
end
end

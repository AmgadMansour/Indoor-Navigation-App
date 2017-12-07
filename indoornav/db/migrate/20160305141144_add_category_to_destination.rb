class AddCategoryToDestination < ActiveRecord::Migration
  def change
    add_column :destinations, :category, :string

    add_index :destinations, :category
  end
end

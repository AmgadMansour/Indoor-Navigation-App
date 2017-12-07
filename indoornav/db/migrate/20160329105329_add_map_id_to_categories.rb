class AddMapIdToCategories < ActiveRecord::Migration
  def change
    add_column :categories, :map_id, :integer

    add_index :categories, :map_id
  end
end

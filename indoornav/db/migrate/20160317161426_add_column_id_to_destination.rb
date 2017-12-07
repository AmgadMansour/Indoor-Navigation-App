class AddColumnIdToDestination < ActiveRecord::Migration
  def change
    add_column :destinations, :category_id, :integer

    change_column_null :destinations, :category_id, false
    add_index :destinations, :category_id
  end
end

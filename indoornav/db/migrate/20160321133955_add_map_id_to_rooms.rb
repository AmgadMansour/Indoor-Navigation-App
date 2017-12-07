class AddMapIdToRooms < ActiveRecord::Migration
  def change
    add_column :rooms, :map_id, :integer

    add_index :rooms, :map_id
  end
end

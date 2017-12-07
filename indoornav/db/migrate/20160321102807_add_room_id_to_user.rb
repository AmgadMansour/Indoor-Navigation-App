class AddRoomIdToUser < ActiveRecord::Migration
  def change
    add_column :users, :room_id, :integer

    add_index :users, :room_id
  end
end

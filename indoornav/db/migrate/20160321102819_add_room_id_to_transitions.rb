class AddRoomIdToTransitions < ActiveRecord::Migration
  def change
    add_column :transitions, :room_id, :integer

    add_index :transitions, :room_id
  end
end

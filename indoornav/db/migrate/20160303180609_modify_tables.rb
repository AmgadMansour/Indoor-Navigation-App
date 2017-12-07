class ModifyTables < ActiveRecord::Migration
  def change
    change_column_null :users, :first_name, false
    change_column_null :users, :last_name, false
    change_column_null :users, :day, false
    change_column_null :users, :month, false
    change_column_null :users, :year, false
    change_column_null :users, :gender, false

    change_column_null :destinations, :name, false
    change_column_null :destinations, :description, false
    change_column_null :destinations, :map_id, false

    change_column_null :graphs, :json, false
    change_column_null :graphs, :map_id, false

    change_column_null :maps, :path, false

    change_column_null :favorites, :user_id, false
    change_column_null :favorites, :destination_id, false

    change_column_null :friends, :sender_id, false
    change_column_null :friends, :receiver_id, false
  end
end

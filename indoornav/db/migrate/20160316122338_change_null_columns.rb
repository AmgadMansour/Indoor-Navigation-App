class ChangeNullColumns < ActiveRecord::Migration
  def change
    change_column_null :transitions, :map_id, false
    change_column_null :beacons, :map_id, false
  end
end

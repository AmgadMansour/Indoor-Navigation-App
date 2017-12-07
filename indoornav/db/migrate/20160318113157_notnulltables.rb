class Notnulltables < ActiveRecord::Migration
  def change
    change_column_null :categories, :name, false
    change_column_null :beacons, :mac_address, false
  end
end

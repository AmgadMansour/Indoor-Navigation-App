class AddMacToBeacons < ActiveRecord::Migration
  def change
    add_column :beacons, :mac_address, :string
  end
end

class FixColumnName < ActiveRecord::Migration
  def self.up
    rename_column :transitions, :map_id_id, :map_id
    rename_column :beacons, :map_id_id, :map_id
  end

  def self.down
  end
end

class CreateBeacons < ActiveRecord::Migration
  def change
    create_table :beacons do |t|
      t.float :x_coordinate
      t.float :y_coordinate
      t.references :map_id, index: true

      t.timestamps null: false
    end
    add_foreign_key :beacons, :map_ids
  end
end

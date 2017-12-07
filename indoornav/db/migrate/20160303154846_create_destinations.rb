class CreateDestinations < ActiveRecord::Migration
  def change
    create_table :destinations do |t|
      t.string :name
      t.text :description
      t.float :x_coordinate
      t.float :y_coordinate
      t.references :map, index: true

      t.timestamps null: false
    end
    add_foreign_key :destinations, :maps
  end
end

class CreateTransitions < ActiveRecord::Migration
  def change
    create_table :transitions do |t|
      t.float :x_coordinate
      t.float :y_coordinate
      t.references :map_id, index: true

      t.timestamps null: false
    end
    add_foreign_key :transitions, :map_ids
  end
end

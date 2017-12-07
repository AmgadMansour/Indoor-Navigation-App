class CreateGraphs < ActiveRecord::Migration
  def change
    create_table :graphs do |t|
      t.text :json
      t.references :map, index: true

      t.timestamps null: false
    end
    add_foreign_key :graphs, :maps
  end
end

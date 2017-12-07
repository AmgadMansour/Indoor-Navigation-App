class CreateMaps < ActiveRecord::Migration
  def change
    create_table :maps do |t|
      t.string :path

      t.timestamps null: false
    end
  end
end

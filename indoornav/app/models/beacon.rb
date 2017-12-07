class Beacon < ActiveRecord::Base
  # Fields

  # Validations
  validates :map_id, :x_coordinate, :y_coordinate, presence: true

  # Relations
  has_one :map

  # Callbacks

  # Class Methods

  # Methods
end

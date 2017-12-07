class Graph < ActiveRecord::Base
  # Fields

  # Validations
  validates :json, :map_id, presence: true

  # Relations
  has_one :map

  # Callbacks

  # Class Methods

  # Methods
  
end

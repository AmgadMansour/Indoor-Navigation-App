class Map < ActiveRecord::Base
  # Fields

  # Validations
  validates :path, presence: true

  # Relations
  belongs_to :beacons
  belongs_to :transitions
  belongs_to :destinations
  belongs_to :rooms
  belongs_to :categories

  # Callbacks

  # Class Methods

  # Methods

end

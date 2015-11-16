
public enum PrimaryEvents {
	///failure or error in a system component or element (example: switch stuck in open position)
	BASIC_EVENT
	///conditions that restrict or affect logic gates (example: mode of operation in effect)
	,CONDITIONING_EVENT
	///an event about which insufficient information is available, or which is of no consequence
	,UNDEVELOPED_EVENT
	///normally expected to occur (not of itself a fault)
	,EXTERNAL_EVENT
	///can be used immediately above a primary event to provide more room to type the event description
	,INTERMEDIATE_EVENT
}
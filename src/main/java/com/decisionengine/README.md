### Decision Engine

The decision engine is in charge of deciding what type of matching it will do to create a response. Engine
take an annotated set and construct a response.

MessageAPI.Recieve() -> Annotations -> DE -----------------> NLG -> MessageAPI.Post()
                                         \_RulesMatcher
                                         \_MLConstructor


Potential Decision Pipeline:
        \_ Rules Check: Check if there exists a rule with a confidence score > threshold. This should be quick a quick lookup.
            if(yes)
                \_send rule to sentence constructor where it will handle it.
            if(no)
                \_ use the Model for sentence generation. Ideally, generates a rule and then sends to sentence constructor.
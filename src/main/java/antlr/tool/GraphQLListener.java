// Generated from /Users/felipereis/git/strongly-typed-schema-builder/src/main/resources/GraphQL.g4 by ANTLR 4.7
package antlr.tool;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GraphQLParser}.
 */
public interface GraphQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(GraphQLParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(GraphQLParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(GraphQLParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(GraphQLParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(GraphQLParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(GraphQLParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#scalarDefinition}.
	 * @param ctx the parse tree
	 */
	void enterScalarDefinition(GraphQLParser.ScalarDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#scalarDefinition}.
	 * @param ctx the parse tree
	 */
	void exitScalarDefinition(GraphQLParser.ScalarDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#interfaceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDefinition(GraphQLParser.InterfaceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#interfaceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDefinition(GraphQLParser.InterfaceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#interfaceFieldSet}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceFieldSet(GraphQLParser.InterfaceFieldSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#interfaceFieldSet}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceFieldSet(GraphQLParser.InterfaceFieldSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#interfaceField}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceField(GraphQLParser.InterfaceFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#interfaceField}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceField(GraphQLParser.InterfaceFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#unionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterUnionDefinition(GraphQLParser.UnionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#unionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitUnionDefinition(GraphQLParser.UnionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#unionMember}.
	 * @param ctx the parse tree
	 */
	void enterUnionMember(GraphQLParser.UnionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#unionMember}.
	 * @param ctx the parse tree
	 */
	void exitUnionMember(GraphQLParser.UnionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#enumDefinition}.
	 * @param ctx the parse tree
	 */
	void enterEnumDefinition(GraphQLParser.EnumDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#enumDefinition}.
	 * @param ctx the parse tree
	 */
	void exitEnumDefinition(GraphQLParser.EnumDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#enumValueSet}.
	 * @param ctx the parse tree
	 */
	void enterEnumValueSet(GraphQLParser.EnumValueSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#enumValueSet}.
	 * @param ctx the parse tree
	 */
	void exitEnumValueSet(GraphQLParser.EnumValueSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinition(GraphQLParser.TypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinition(GraphQLParser.TypeDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#typeFieldSet}.
	 * @param ctx the parse tree
	 */
	void enterTypeFieldSet(GraphQLParser.TypeFieldSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#typeFieldSet}.
	 * @param ctx the parse tree
	 */
	void exitTypeFieldSet(GraphQLParser.TypeFieldSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#typeField}.
	 * @param ctx the parse tree
	 */
	void enterTypeField(GraphQLParser.TypeFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#typeField}.
	 * @param ctx the parse tree
	 */
	void exitTypeField(GraphQLParser.TypeFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(GraphQLParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(GraphQLParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgument(GraphQLParser.TypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgument(GraphQLParser.TypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#interfaceName}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceName(GraphQLParser.InterfaceNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#interfaceName}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceName(GraphQLParser.InterfaceNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#inputDefinition}.
	 * @param ctx the parse tree
	 */
	void enterInputDefinition(GraphQLParser.InputDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#inputDefinition}.
	 * @param ctx the parse tree
	 */
	void exitInputDefinition(GraphQLParser.InputDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#inputFieldSet}.
	 * @param ctx the parse tree
	 */
	void enterInputFieldSet(GraphQLParser.InputFieldSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#inputFieldSet}.
	 * @param ctx the parse tree
	 */
	void exitInputFieldSet(GraphQLParser.InputFieldSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#inputField}.
	 * @param ctx the parse tree
	 */
	void enterInputField(GraphQLParser.InputFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#inputField}.
	 * @param ctx the parse tree
	 */
	void exitInputField(GraphQLParser.InputFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#operationDefinition}.
	 * @param ctx the parse tree
	 */
	void enterOperationDefinition(GraphQLParser.OperationDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#operationDefinition}.
	 * @param ctx the parse tree
	 */
	void exitOperationDefinition(GraphQLParser.OperationDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#selectionSet}.
	 * @param ctx the parse tree
	 */
	void enterSelectionSet(GraphQLParser.SelectionSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#selectionSet}.
	 * @param ctx the parse tree
	 */
	void exitSelectionSet(GraphQLParser.SelectionSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#operationType}.
	 * @param ctx the parse tree
	 */
	void enterOperationType(GraphQLParser.OperationTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#operationType}.
	 * @param ctx the parse tree
	 */
	void exitOperationType(GraphQLParser.OperationTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#selection}.
	 * @param ctx the parse tree
	 */
	void enterSelection(GraphQLParser.SelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#selection}.
	 * @param ctx the parse tree
	 */
	void exitSelection(GraphQLParser.SelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(GraphQLParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(GraphQLParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(GraphQLParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(GraphQLParser.FieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(GraphQLParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(GraphQLParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(GraphQLParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(GraphQLParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(GraphQLParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(GraphQLParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#fragmentSpread}.
	 * @param ctx the parse tree
	 */
	void enterFragmentSpread(GraphQLParser.FragmentSpreadContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#fragmentSpread}.
	 * @param ctx the parse tree
	 */
	void exitFragmentSpread(GraphQLParser.FragmentSpreadContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#inlineFragment}.
	 * @param ctx the parse tree
	 */
	void enterInlineFragment(GraphQLParser.InlineFragmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#inlineFragment}.
	 * @param ctx the parse tree
	 */
	void exitInlineFragment(GraphQLParser.InlineFragmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#fragmentDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFragmentDefinition(GraphQLParser.FragmentDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#fragmentDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFragmentDefinition(GraphQLParser.FragmentDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#fragmentName}.
	 * @param ctx the parse tree
	 */
	void enterFragmentName(GraphQLParser.FragmentNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#fragmentName}.
	 * @param ctx the parse tree
	 */
	void exitFragmentName(GraphQLParser.FragmentNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#directives}.
	 * @param ctx the parse tree
	 */
	void enterDirectives(GraphQLParser.DirectivesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#directives}.
	 * @param ctx the parse tree
	 */
	void exitDirectives(GraphQLParser.DirectivesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#directive}.
	 * @param ctx the parse tree
	 */
	void enterDirective(GraphQLParser.DirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#directive}.
	 * @param ctx the parse tree
	 */
	void exitDirective(GraphQLParser.DirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#typeCondition}.
	 * @param ctx the parse tree
	 */
	void enterTypeCondition(GraphQLParser.TypeConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#typeCondition}.
	 * @param ctx the parse tree
	 */
	void exitTypeCondition(GraphQLParser.TypeConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#variableDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterVariableDefinitions(GraphQLParser.VariableDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#variableDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitVariableDefinitions(GraphQLParser.VariableDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void enterVariableDefinition(GraphQLParser.VariableDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void exitVariableDefinition(GraphQLParser.VariableDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(GraphQLParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(GraphQLParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(GraphQLParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(GraphQLParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#valueOrVariable}.
	 * @param ctx the parse tree
	 */
	void enterValueOrVariable(GraphQLParser.ValueOrVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#valueOrVariable}.
	 * @param ctx the parse tree
	 */
	void exitValueOrVariable(GraphQLParser.ValueOrVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterStringValue(GraphQLParser.StringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitStringValue(GraphQLParser.StringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterNumberValue(GraphQLParser.NumberValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitNumberValue(GraphQLParser.NumberValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(GraphQLParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(GraphQLParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterArrayValue(GraphQLParser.ArrayValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitArrayValue(GraphQLParser.ArrayValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code enumValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterEnumValue(GraphQLParser.EnumValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code enumValue}
	 * labeled alternative in {@link GraphQLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitEnumValue(GraphQLParser.EnumValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(GraphQLParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(GraphQLParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(GraphQLParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(GraphQLParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#listType}.
	 * @param ctx the parse tree
	 */
	void enterListType(GraphQLParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#listType}.
	 * @param ctx the parse tree
	 */
	void exitListType(GraphQLParser.ListTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#nonNullType}.
	 * @param ctx the parse tree
	 */
	void enterNonNullType(GraphQLParser.NonNullTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#nonNullType}.
	 * @param ctx the parse tree
	 */
	void exitNonNullType(GraphQLParser.NonNullTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GraphQLParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(GraphQLParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link GraphQLParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(GraphQLParser.ArrayContext ctx);
}
package me.serce.solidity.lang.completion

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.StubIndex
import me.serce.solidity.ide.SolidityIcons
import me.serce.solidity.lang.psi.*
import me.serce.solidity.lang.resolve.SolResolver
import me.serce.solidity.lang.stubs.SolGotoClassIndex
import me.serce.solidity.lang.stubs.SolModifierIndex
import me.serce.solidity.lang.types.SolStruct
import me.serce.solidity.lang.types.type

object SolCompleter {
  fun completeTypeName(element: SolUserDefinedTypeName): Array<out LookupElement> {
    val project = element.project
    val allTypeNames = StubIndex.getInstance().getAllKeys(
      SolGotoClassIndex.KEY,
      project
    )
    return allTypeNames
      .map { LookupElementBuilder.create(it, it).withIcon(SolidityIcons.CONTRACT) }
      .toTypedArray()
  }

  fun completeModifier(element: PsiElement): Array<out LookupElement> {
    val project = element.project
    val allModifiers = StubIndex.getInstance().getAllKeys(
      SolModifierIndex.KEY,
      project
    )
    return allModifiers
      .map { LookupElementBuilder.create(it, it).withIcon(SolidityIcons.FUNCTION) }
      .toTypedArray()
  }

  fun completeLiteral(element: SolVarLiteral): Array<out LookupElement> {
    val toList = SolResolver.lexicalDeclarations(element).take(25)
    return toList.createLookups()
  }

  fun completeMemberAccess(element: SolMemberAccessExpression): Array<out LookupElement> {
    val exprType = element.expression.type
    return when(exprType) {
      is SolStruct -> emptyArray()
      else -> emptyArray()
    }
  }

  private fun Sequence<SolNamedElement>.createLookups(): Array<LookupElementBuilder> {
    return toList()
      .map { LookupElementBuilder.create(it, it.name ?: "").withIcon(SolidityIcons.STATE_VAR) }
      .toTypedArray()
  }
}

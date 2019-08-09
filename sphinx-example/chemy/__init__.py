"""Basic chemistry module.

The :mod:`chemistry` module contains three classes:

- :class:`chemistry.Atom`
- :class:`chemistry.Bond`
- :class:`chemistry.Molecule`

One can use the :func:`chemistry.Molecule.add_atom` and
:func:`chemsitry.Molecule.add_bond` functions to build up a molecule.

Example illustrating how to create a methane molecule.

>>> from chemistry import Molecule
>>> mol = Molecule('Methane')
>>> carbon_index = mol.add_atom(atomic_number=6)
>>> hydrogen1_index = mol.add_atom(atomic_number=1)
>>> hydrogen2_index = mol.add_atom(atomic_number=1)
>>> hydrogen3_index = mol.add_atom(atomic_number=1)
>>> hydrogen4_index = mol.add_atom(atomic_number=1)
>>> bond1_index = mol.add_bond(carbon_index, hydrogen1_index)
>>> bond2_index = mol.add_bond(carbon_index, hydrogen2_index)
>>> bond3_index = mol.add_bond(carbon_index, hydrogen3_index)
>>> bond4_index = mol.add_bond(carbon_index, hydrogen4_index)
"""

class Atom(object):
    """Class representing an atom."""

    def __init__(self, atomic_number):
        self.atomic_number = atomic_number
        self.bonds = []

    def bond_to(self, other_atom):
        """Return the :class:`chemistry.Bond` formed between the two atoms.

        :param other_atom: :class:`chemistry.Atom` to form :class:`chemistry.Bond` to
        :returns: :class:`chemistry.Bond`
        """
        bond = Bond(self, other_atom)
        self.bonds.append(bond)
        other_atom.bonds.append(bond)
        return bond

class Bond(object):
    """Class representing a bond between two atoms."""
    
    def __init__(self, atom1, atom2):
        self.atoms = (atom1, atom2)

class Molecule(object):
    """Class representing a molecule consisting of atoms and bonds."""

    def __init__(self, identifier):
        self.identifier = identifier
        self.atoms = []
        self.bonds = []

    def add_atom(self, atomic_number):
        """Return the list index of the atom added to the molecule.

        :param atomic_number: atomic number of the atom to be added
        :returns: index of the atom in the molecule
        """
        atom = Atom(atomic_number)
        self.atoms.append(atom)
        return len(self.atoms) - 1

    def add_bond(self, atom1_index, atom2_index):
        """Return the list index of the bond added to the molecule.

        :param atom1_index: atom's index in molecule
        :param atom2_index: atom's index in molecule
        :returns: index of the bond in the molecule
        """
        atom1 = self.atoms[atom1_index]
        atom2 = self.atoms[atom2_index]
        bond = atom1.bond_to(atom2)
        self.bonds.append(bond)
        return len(self.bonds) - 1

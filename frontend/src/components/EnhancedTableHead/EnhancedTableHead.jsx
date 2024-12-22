import Checkbox from '@material-ui/core/Checkbox';
//import PropTypes from 'prop-types';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Tooltip from '@material-ui/core/Tooltip';
import React from 'react';
import './EnhancedTableHead.css';



class EnhancedTableHead extends React.Component {
  createSortHandler = property => event => {
    this.props.onRequestSort(event, property);
  };

  render() {
    const { onSelectAllClick, order, orderBy, numSelected, rowCount, rows } = this.props;
    return (
      <TableHead>
        <TableRow>
          {false ?
            <TableCell padding="checkbox">
              <Checkbox
                indeterminate={numSelected > 0 && numSelected < rowCount}
                checked={numSelected === rowCount}
                onChange={onSelectAllClick}
              />
            </TableCell>
            : null
          }

          {rows.map(
            row => (
              <TableCell
                key={`TableCell-${row.id}-${Math.random()}`}
                //align={row.numeric ? 'right' : 'left'}
                align={'center'}
                padding={row.disablePadding ? 'none' : 'default'}
                sortDirection={orderBy === row.id ? order : false}
                className="text-header"
              >

                {row.isSorted ?
                  <Tooltip
                    title="Sort"
                    placement={row.numeric ? 'bottom-end' : 'bottom-start'}
                    enterDelay={300}>
                    <TableSortLabel
                      color = {"primary"}
                      active={orderBy === row.id}
                      direction={(""+order).toLowerCase()}
                      onClick={this.createSortHandler(row.id)}>
                      <span style={{ fontSize: 15 }}>{row.label}</span>
                    </TableSortLabel>
                  </Tooltip>
                  : <span style={{ fontSize: 15, marginLeft: 5 }}>{`${row.label} `}</span>
                }

              </TableCell>
            ),
            this,
          )}
        </TableRow>
      </TableHead>
    );
  }
}

/*EnhancedTableHead.propTypes = {
  numSelected: PropTypes.number,
  onRequestSort: PropTypes.func,
  onSelectAllClick: PropTypes.func,
  order: PropTypes.string.isRequired,
  orderBy: PropTypes.string.isRequired,
  rowCount: PropTypes.number.isRequired,
  rows: PropTypes.array.isRequired
};*/

export default EnhancedTableHead;